-- ACTIVE 좋아요 생성
INSERT INTO product_like (product_id, user_id, status, created_at, updated_at, deleted_at)
SELECT
    p.id,
    FLOOR(1 + RAND() * 50000) AS user_id,
    'ACTIVE' AS status,
    NOW(6), NOW(6), NULL
FROM product p
         JOIN (
    SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
) t
WHERE RAND() < 0.05;

-- 일부를 DELETED로 업데이트
UPDATE product_like
SET status = 'DELETED',
    deleted_at = NOW(6)
WHERE RAND() < 0.05;