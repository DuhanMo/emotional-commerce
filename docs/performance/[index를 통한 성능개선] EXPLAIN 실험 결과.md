## TL;DR; 실행 시간 비교

| 쿼리 | AS-IS(ms) | TO-BE(ms) | 개선폭(ms) | 개선율 |
| --- | --- | --- | --- | --- |
| 1-1A (서브쿼리 집계조인) | 6108 | 1629 | 4479 | 73.33% |
| 1-1B (조인+GROUP BY) | 1692 | 29.3 | 1662.7 | 98.27% |
| 1-2  (브랜드+가격 정렬) | 245 | 0.736 | 244.264 | 99.70% |
| 1-3  (전역 인기) | 7017 | 3145 | 3872 | 55.18% |

데이터셋

- product: 100만건
- product_like: 700만건

# AS-IS 성능 측정 (인덱스 없이)

## 1. Case

### 1-1. 브랜드 필터 + “좋아요 수 내림차순” 정렬 (가장 비싼 케이스)

**A안: 집계 서브쿼리 후 조인**

```sql
EXPLAIN ANALYZE
SELECT p.id, p.brand_id, p.name, p.price, COALESCE(lc.like_cnt, 0) AS like_cnt
FROM product p
LEFT JOIN (
  SELECT product_id, COUNT(*) AS like_cnt
  FROM product_like
  WHERE status = 'ACTIVE'
  GROUP BY product_id
) lc ON lc.product_id = p.id
WHERE p.brand_id = 55                 
ORDER BY like_cnt DESC, p.id DESC
LIMIT 20 OFFSET 0;
```

**B안: 바로 조인 + 그룹바이**

```sql
EXPLAIN ANALYZE
SELECT p.id, p.brand_id, p.name, p.price, COUNT(pl.id) AS like_cnt
FROM product p
LEFT JOIN product_like pl
  ON pl.product_id = p.id AND pl.status = 'ACTIVE'
WHERE p.brand_id = 55
GROUP BY p.id
ORDER BY like_cnt DESC, p.id DESC
LIMIT 20 OFFSET 0;
```

---

### 1-2. 브랜드 필터 + 가격 오름차순 정렬 (비교용)

```sql
EXPLAIN ANALYZE
SELECT p.id, p.brand_id, p.name, p.price
FROM product p
WHERE p.brand_id = 55
ORDER BY p.price ASC, p.id ASC
LIMIT 20 OFFSET 0;
```

---

### 1-3. 전체 인기 상품 (브랜드 필터 없이 좋아요 순)

```sql
EXPLAIN ANALYZE
SELECT p.id, p.brand_id, p.name, COALESCE(lc.like_cnt, 0) AS like_cnt
FROM product p
LEFT JOIN (
  SELECT product_id, COUNT(*) AS like_cnt
  FROM product_like
  WHERE status = 'ACTIVE'
  GROUP BY product_id
) lc ON lc.product_id = p.id
ORDER BY like_cnt DESC, p.id DESC
LIMIT 20 OFFSET 0;
```

---

## 2. 실험 기록

**1-1A [브랜드 필터 + “좋아요 수 내림차순” 정렬, 집계 서브쿼리 후 조인] 실행 기록:**

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | PRIMARY | p | null | ALL | null | null | null | null | 989888 | 10 | Using where; Using temporary; Using filesort |
| 1 | PRIMARY | <derived2> | null | ref | <auto_key0> | <auto_key0> | 8 | [loopers.p.id](http://loopers.p.id/) | 10 | 100 | null |
| 2 | DERIVED | product_like | null | ALL | null | null | null | null | 4859182 | 50 | Using where; Using temporary |

```sql
-> Limit: 20 row(s)  (actual time=6108..6108 rows=20 loops=1)
    -> Sort: like_cnt DESC, p.id DESC, limit input to 20 row(s) per chunk  (actual time=6108..6108 rows=20 loops=1)
        -> Stream results  (cost=355676 rows=0) (actual time=5894..6108 rows=5000 loops=1)
            -> Nested loop left join  (cost=355676 rows=0) (actual time=5894..6107 rows=5000 loops=1)
                -> Filter: (p.brand_id = 55)  (cost=108204 rows=98989) (actual time=100..307 rows=5000 loops=1)
                    -> Table scan on p  (cost=108204 rows=989888) (actual time=0.358..282 rows=1e+6 loops=1)
                -> Index lookup on lc using <auto_key0> (product_id=p.id)  (cost=0.25..2.5 rows=10) (actual time=1.16..1.16 rows=1 loops=5000)
                    -> Materialize  (cost=0..0 rows=0) (actual time=5794..5794 rows=999999 loops=1)
                        -> Table scan on <temporary>  (actual time=5032..5140 rows=999999 loops=1)
                            -> Aggregate using temporary table  (actual time=5032..5032 rows=999998 loops=1)
                                -> Filter: (product_like.`status` = 'ACTIVE')  (cost=501962 rows=2.43e+6) (actual time=0.624..1101 rows=4.75e+6 loops=1)
                                    -> Table scan on product_like  (cost=501962 rows=4.86e+6) (actual time=0.616..778 rows=5e+6 loops=1)
```

**1-1B [브랜드 필터 + “좋아요 수 내림차순” 정렬, 바로 조인 + 그룹바이] 실행 시간:**

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | SIMPLE | p | null | ALL | PRIMARY | null | null | null | 989888 | 10 | Using where; Using temporary; Using filesort |
| 1 | SIMPLE | pl | null | ALL | null | null | null | null | 4859182 | 100 | Using where; Using join buffer \(hash join\) |

```sql
-> Limit: 20 row(s)  (actual time=1692..1692 rows=20 loops=1)
    -> Sort: like_cnt DESC, p.id DESC, limit input to 20 row(s) per chunk  (actual time=1692..1692 rows=20 loops=1)
        -> Table scan on <temporary>  (actual time=1691..1691 rows=5000 loops=1)
            -> Aggregate using temporary table  (actual time=1691..1691 rows=5000 loops=1)
                -> Left hash join (pl.product_id = p.id)  (cost=48.1e+9 rows=481e+9) (actual time=1431..1685 rows=23732 loops=1)
                    -> Filter: (p.brand_id = 55)  (cost=107258 rows=98989) (actual time=58.6..217 rows=5000 loops=1)
                        -> Table scan on p  (cost=107258 rows=989888) (actual time=0.0393..195 rows=1e+6 loops=1)
                    -> Hash
                        -> Filter: (pl.`status` = 'ACTIVE')  (cost=85 rows=4.86e+6) (actual time=0.579..917 rows=4.75e+6 loops=1)
                            -> Table scan on pl  (cost=85 rows=4.86e+6) (actual time=0.576..617 rows=5e+6 loops=1)
```

**1-2 [브랜드 필터 + 가격 오름차순 정렬] 실행 시간:**

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | SIMPLE | p | null | ALL | null | null | null | null | 989888 | 10 | Using where; Using filesort |

```sql
-> Limit: 20 row(s)  (cost=106488 rows=20) (actual time=245..245 rows=20 loops=1)
    -> Sort: p.price, p.id, limit input to 20 row(s) per chunk  (cost=106488 rows=989888) (actual time=245..245 rows=20 loops=1)
        -> Filter: (p.brand_id = 55)  (cost=106488 rows=989888) (actual time=83.6..245 rows=5000 loops=1)
            -> Table scan on p  (cost=106488 rows=989888) (actual time=0.078..220 rows=1e+6 loops=1)
```

**1-3 [전체 인기 상품] 실행 시간:**

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | PRIMARY | p | null | ALL | null | null | null | null | 989888 | 100 | Using temporary; Using filesort |
| 1 | PRIMARY | <derived2> | null | ref | <auto_key0> | <auto_key0> | 8 | [loopers.p.id](http://loopers.p.id/) | 10 | 100 | null |
| 2 | DERIVED | product_like | null | ALL | null | null | null | null | 4859182 | 50 | Using where; Using temporary |

```sql
-> Limit: 20 row(s)  (actual time=7017..7017 rows=20 loops=1)
    -> Sort: like_cnt DESC, p.id DESC, limit input to 20 row(s) per chunk  (actual time=7017..7017 rows=20 loops=1)
        -> Stream results  (cost=2.58e+6 rows=0) (actual time=5531..6947 rows=1e+6 loops=1)
            -> Nested loop left join  (cost=2.58e+6 rows=0) (actual time=5531..6831 rows=1e+6 loops=1)
                -> Table scan on p  (cost=106488 rows=989888) (actual time=0.13..205 rows=1e+6 loops=1)
                -> Index lookup on lc using <auto_key0> (product_id=p.id)  (cost=0.25..2.5 rows=10) (actual time=0.00643..0.00653 rows=1 loops=1e+6)
                    -> Materialize  (cost=0..0 rows=0) (actual time=5531..5531 rows=999999 loops=1)
                        -> Table scan on <temporary>  (actual time=4789..4895 rows=999999 loops=1)
                            -> Aggregate using temporary table  (actual time=4789..4789 rows=999998 loops=1)
                                -> Filter: (product_like.`status` = 'ACTIVE')  (cost=505550 rows=2.43e+6) (actual time=1.1..995 rows=4.75e+6 loops=1)
                                    -> Table scan on product_like  (cost=505550 rows=4.86e+6) (actual time=1.1..677 rows=5e+6 loops=1)
```

# TO-BE 인덱스 생성

```sql
-- 브랜드 필터 + 가격 정렬 최적화
CREATE INDEX idx_product_brand_price_id
  ON product (brand_id, price, id);

-- 좋아요 집계/조인 최적화
CREATE INDEX idx_pl_status_product
  ON product_like (status, product_id);
```

**1-1A [브랜드 필터 + “좋아요 수 내림차순” 정렬, 집계 서브쿼리 후 조인] 재실행 기록:**

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | PRIMARY | p | null | ref | idx_product_brand_price_id | idx_product_brand_price_id | 8 | const | 5000 | 100 | Using temporary; Using filesort |
| 1 | PRIMARY | <derived2> | null | ref | <auto_key0> | <auto_key0> | 8 | [loopers.p.id](http://loopers.p.id/) | 10 | 100 | null |
| 2 | DERIVED | product_like | null | ref | idx_pl_status_product | idx_pl_status_product | 2 | const | 2429591 | 100 | Using where; Using index |

```sql
-> Limit: 20 row(s)  (actual time=1629..1629 rows=20 loops=1)
    -> Sort: like_cnt DESC, p.id DESC, limit input to 20 row(s) per chunk  (actual time=1629..1629 rows=20 loops=1)
        -> Stream results  (cost=1.12e+6 rows=11e+6) (actual time=1619..1628 rows=5000 loops=1)
            -> Nested loop left join  (cost=1.12e+6 rows=11e+6) (actual time=1619..1628 rows=5000 loops=1)
                -> Index lookup on p using idx_product_brand_price_id (brand_id=55)  (cost=5481 rows=5000) (actual time=0.0783..2.28 rows=5000 loops=1)
                -> Index lookup on lc using <auto_key0> (product_id=p.id)  (cost=491468..491470 rows=10) (actual time=0.325..0.325 rows=1 loops=5000)
                    -> Materialize  (cost=491468..491468 rows=2204) (actual time=1619..1619 rows=999999 loops=1)
                        -> Group aggregate: count(0)  (cost=491247 rows=2204) (actual time=0.355..967 rows=999999 loops=1)
                            -> Filter: (product_like.`status` = 'ACTIVE')  (cost=248288 rows=2.43e+6) (actual time=0.35..784 rows=4.75e+6 loops=1)
                                -> Covering index lookup on product_like using idx_pl_status_product (status='ACTIVE')  (cost=248288 rows=2.43e+6) (actual time=0.349..479 rows=4.75e+6 loops=1)

```

**1-1B [브랜드 필터 + “좋아요 수 내림차순” 정렬, 바로 조인 + 그룹바이] 재실행 시간:**

| id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | SIMPLE | p | null | ref | PRIMARY,idx\_product\_brand\_price\_id | idx\_product\_brand\_price\_id | 8 | const | 5000 | 100 | Using temporary; Using filesort |
| 1 | SIMPLE | pl | null | ref | idx\_pl\_status\_product | idx\_pl\_status\_product | 10 | const,[loopers.p.id](http://loopers.p.id/) | 3 | 100 | Using where; Using index |

```sql
-> Limit: 20 row(s)  (actual time=29.3..29.3 rows=20 loops=1)
    -> Sort: like_cnt DESC, p.id DESC, limit input to 20 row(s) per chunk  (actual time=29.3..29.3 rows=20 loops=1)
        -> Table scan on <temporary>  (actual time=28.5..28.9 rows=5000 loops=1)
            -> Aggregate using temporary table  (actual time=28.5..28.5 rows=5000 loops=1)
                -> Nested loop left join  (cost=12502 rows=19885) (actual time=0.104..19.7 rows=23732 loops=1)
                    -> Index lookup on p using idx_product_brand_price_id (brand_id=55)  (cost=5481 rows=5000) (actual time=0.0644..3.91 rows=5000 loops=1)
                    -> Filter: (pl.`status` = 'ACTIVE')  (cost=1.01 rows=3.98) (actual time=0.00195..0.00285 rows=4.75 loops=5000)
                        -> Covering index lookup on pl using idx_pl_status_product (status='ACTIVE', product_id=p.id)  (cost=1.01 rows=3.98) (actual time=0.0018..0.00229 rows=4.75 loops=5000)

```

**1-2 [브랜드 필터 + 가격 오름차순 정렬] 재실행 시간:**

| id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | SIMPLE | p | null | ref | idx\_product\_brand\_price\_id | idx\_product\_brand\_price\_id | 8 | const | 5000 | 100 | null |

```sql
-> Limit: 20 row(s)  (cost=5481 rows=20) (actual time=0.723..0.736 rows=20 loops=1)
    -> Index lookup on p using idx_product_brand_price_id (brand_id=55)  (cost=5481 rows=5000) (actual time=0.71..0.722 rows=20 loops=1)
```

**1-3 [전체 인기 상품] 재실행 시간:**

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | PRIMARY | p | null | ALL | null | null | null | null | 989888 | 100 | Using temporary; Using filesort |
| 1 | PRIMARY | <derived2> | null | ref | <auto_key0> | <auto_key0> | 8 | [loopers.p.id](http://loopers.p.id/) | 10 | 100 | null |
| 2 | DERIVED | product_like | null | ref | idx_pl_status_product | idx_pl_status_product | 2 | const | 2429591 | 100 | Using where; Using index |

```sql
-> Limit: 20 row(s)  (actual time=3145..3145 rows=20 loops=1)
    -> Sort: like_cnt DESC, p.id DESC, limit input to 20 row(s) per chunk  (actual time=3145..3145 rows=20 loops=1)
        -> Stream results  (cost=221e+6 rows=2.18e+9) (actual time=1631..3063 rows=1e+6 loops=1)
            -> Nested loop left join  (cost=221e+6 rows=2.18e+9) (actual time=1631..2951 rows=1e+6 loops=1)
                -> Table scan on p  (cost=108724 rows=989888) (actual time=0.817..252 rows=1e+6 loops=1)
                -> Index lookup on lc using <auto_key0> (product_id=p.id)  (cost=491468..491470 rows=10) (actual time=0.00251..0.00261 rows=1 loops=1e+6)
                    -> Materialize  (cost=491468..491468 rows=2204) (actual time=1630..1630 rows=999999 loops=1)
                        -> Group aggregate: count(0)  (cost=491247 rows=2204) (actual time=0.77..975 rows=999999 loops=1)
                            -> Filter: (product_like.`status` = 'ACTIVE')  (cost=248288 rows=2.43e+6) (actual time=0.761..802 rows=4.75e+6 loops=1)
                                -> Covering index lookup on product_like using idx_pl_status_product (status='ACTIVE')  (cost=248288 rows=2.43e+6) (actual time=0.75..511 rows=4.75e+6 loops=1)

```