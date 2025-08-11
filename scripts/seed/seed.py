import os, random
from faker import Faker
from mysql.connector import connect
from dotenv import load_dotenv
from datetime import datetime

load_dotenv()

DB_HOST = os.getenv("DB_HOST", "127.0.0.1")
DB_PORT = int(os.getenv("DB_PORT", "3306"))
DB_NAME = os.getenv("DB_NAME", "loopers")
DB_USER = os.getenv("DB_USER", "application")
DB_PASS = os.getenv("DB_PASS", "application")

BRANDS = int(os.getenv("BRANDS", "100"))
PRODUCTS_PER_BRAND = int(os.getenv("PRODUCTS_PER_BRAND", "50"))
BATCH_SIZE = int(os.getenv("BATCH_SIZE", "2000"))

faker = Faker()

def log(msg):
    print(f"[{datetime.now().strftime('%H:%M:%S')}] {msg}")

def has_any(cur, table):
    cur.execute(f"SELECT 1 FROM {table} WHERE deleted_at IS NULL LIMIT 1")
    return cur.fetchone() is not None

def exec_batch(cur, sql, rows, label=None):
    cur.executemany(sql, rows)
    if label:
        log(f"{label}: {len(rows)} rows inserted")

def seed():
    cnx = connect(
        host=DB_HOST, port=DB_PORT,
        user=DB_USER, password=DB_PASS,
        database=DB_NAME
    )
    cnx.autocommit = False
    cur = cnx.cursor()

    try:
        has_brand = has_any(cur, "brand")
        has_product = has_any(cur, "product")
        has_summary = has_any(cur, "product_summary")
        if has_brand or has_product or has_summary:
            log(f"Skip: existing data (brand={has_brand}, product={has_product}, summary={has_summary})")
            cnx.rollback()
            return

        # 1) Brands
        brand_sql = """
            INSERT INTO brand (name, description, logo_url, created_at, updated_at, deleted_at)
            VALUES (%s, %s, %s, NOW(), NOW(), NULL)
        """
        brand_rows = []
        log(f"Seeding {BRANDS} brands...")
        for i in range(BRANDS):
            name = f'{faker.company().replace("'", "")} #{i+1}'
            brand_rows.append((
                name,
                faker.text(max_nb_chars=80),
                f"https://picsum.photos/seed/brand_{i+1}/200/200"
            ))
            if len(brand_rows) >= BATCH_SIZE:
                exec_batch(cur, brand_sql, brand_rows, "Brands")
                brand_rows.clear()
        if brand_rows:
            exec_batch(cur, brand_sql, brand_rows, "Brands")

        cur.execute("SELECT id FROM brand WHERE deleted_at IS NULL ORDER BY id")
        brand_ids = [row[0] for row in cur.fetchall()]
        log(f"Total brands inserted: {len(brand_ids)}")

        # 2) Products
        product_sql = """
            INSERT INTO product (brand_id, name, description, price, stock, image_url, created_at, updated_at, deleted_at)
            VALUES (%s, %s, %s, %s, %s, %s, NOW(), NOW(), NULL)
        """
        product_rows = []
        log(f"Seeding products ({PRODUCTS_PER_BRAND} per brand)...")
        for b_id in brand_ids:
            for _ in range(PRODUCTS_PER_BRAND):
                product_rows.append((
                    b_id,
                    faker.word()[:100],
                    faker.text(max_nb_chars=120),
                    random.randint(5_000, 500_000),
                    random.randint(0, 1000),
                    f"https://picsum.photos/seed/product_{b_id}_{_}/400/400",
                ))
                if len(product_rows) >= BATCH_SIZE:
                    exec_batch(cur, product_sql, product_rows, "Products")
                    product_rows.clear()
        if product_rows:
            exec_batch(cur, product_sql, product_rows, "Products")

        cur.execute("SELECT id FROM product WHERE deleted_at IS NULL ORDER BY id")
        product_ids = [row[0] for row in cur.fetchall()]
        log(f"Total products inserted: {len(product_ids)}")

        # 3) Product summaries
        summary_sql = """
            INSERT INTO product_summary (product_id, like_count, version, created_at, updated_at, deleted_at)
            VALUES (%s, %s, 0, NOW(), NOW(), NULL)
        """
        summary_rows = []
        log("Seeding product summaries...")
        for pid in product_ids:
            like_count = random.randint(0, 50) if pid % 3 == 0 else 0
            summary_rows.append((pid, like_count))
            if len(summary_rows) >= BATCH_SIZE:
                exec_batch(cur, summary_sql, summary_rows, "Summaries")
                summary_rows.clear()
        if summary_rows:
            exec_batch(cur, summary_sql, summary_rows, "Summaries")

        cnx.commit()
        log(f"Seed completed: brands={len(brand_ids)}, products={len(product_ids)}, summaries={len(product_ids)}")
    except Exception as e:
        cnx.rollback()
        raise
    finally:
        cur.close()
        cnx.close()

if __name__ == "__main__":
    seed()