import os
import random
from datetime import datetime

from dotenv import load_dotenv
from faker import Faker
from mysql.connector import connect

load_dotenv()
faker = Faker()

# ==== 환경 변수 ====
DB_HOST = os.getenv("DB_HOST", "127.0.0.1")
DB_PORT = int(os.getenv("DB_PORT", "3306"))
DB_NAME = os.getenv("DB_NAME", "loopers")
DB_USER = os.getenv("DB_USER", "application")
DB_PASS = os.getenv("DB_PASS", "application")

# 데이터 볼륨/분포
BRANDS = int(os.getenv("BRANDS", "100"))                 # brand_id는 1..BRANDS 범위에서 무작위
TOTAL_PRODUCTS = int(os.getenv("TOTAL_PRODUCTS", "10000000"))
BATCH_SIZE = int(os.getenv("BATCH_SIZE", "2000"))


def log(msg: str):
    print(f"[{datetime.now().strftime('%H:%M:%S')}] {msg}")

def has_any(cur, table: str) -> bool:
    cur.execute(f"SELECT 1 FROM {table} WHERE deleted_at IS NULL LIMIT 1")
    return cur.fetchone() is not None

def exec_batch(cur, sql: str, rows: list, label: str = None):
    if not rows:
        return
    cur.executemany(sql, rows)
    if label:
        log(f"{label}: {len(rows)} rows inserted")

def seed():
    cnx = connect(
        host=DB_HOST, port=DB_PORT,
        user=DB_USER, password=DB_PASS,
        database=DB_NAME,
        autocommit=False,
    )
    cur = cnx.cursor()

    try:
        has_product = has_any(cur, "product")
        has_like = has_any(cur, "product_like")

        if has_product or has_like:
            log(f"Skip: existing data (product={has_product}, product_like={has_like})")
            cnx.rollback()
            return

        # 1) product
        product_sql = """
            INSERT INTO product
                (brand_id, name, description, price, stock, image_url, created_at, updated_at, deleted_at)
            VALUES
                (%s, %s, %s, %s, %s, %s, NOW(6), NOW(6), NULL)
        """
        product_rows = []
        log(f"Seeding products: total={TOTAL_PRODUCTS}, brands={BRANDS} ...")

        for i in range(TOTAL_PRODUCTS):
            brand_id = random.randint(1, BRANDS)
            name = faker.word()[:100]
            desc = faker.text(max_nb_chars=150)
            price = random.randint(5_000, 500_000)
            stock = random.randint(0, 1000)
            image_url = f"https://picsum.photos/seed/product_{i+1}/400/400"

            product_rows.append((brand_id, name, desc, price, stock, image_url))

            if len(product_rows) >= BATCH_SIZE:
                exec_batch(cur, product_sql, product_rows, "Products")
                product_rows.clear()

        exec_batch(cur, product_sql, product_rows, "Products")

        # 제품 ID 전체 조회
        cur.execute("SELECT id FROM product WHERE deleted_at IS NULL ORDER BY id")
        product_ids = [row[0] for row in cur.fetchall()]
        log(f"Total products inserted: {len(product_ids)}")


        cnx.commit()
        log("Seed completed.")
    except Exception:
        cnx.rollback()
        raise
    finally:
        cur.close()
        cnx.close()

if __name__ == "__main__":
    seed()
