49.
SELECT
  SUM(残高) AS 残高の合計,
  MAX(残高) AS 最大,
  MIN(残高) AS 最小,
  AVG(残高) AS 平均,
  COUNT(*) AS データ件数
FROM
  口座
;

50.
SELECT
  COUNT(*)
FROM
  口座
WHERE
  種別 <> '1'
  AND 残高 >= 1000000
  AND 更新日 < '2013-01-01'
;

51.
SELECT
  COUNT(*) - COUNT(更新日)
FROM
  口座
;

52.
SELECT
  MAX(名義),
  MIN(名義)
FROM
  口座
;

53.
SELECT
  MAX(更新日),
  MIN(更新日)
FROM
  口座
;

54.
SELECT
  種別,
  SUM(残高) AS 残高の合計,
  MAX(残高) AS 最大,
  MIN(残高) AS 最小,
  AVG(残高) AS 平均,
  COUNT(*) AS データ件数
FROM
  口座
GROUP BY
  種別
;

55.
SELECT
  SUBSTRING(口座番号, 7, 1) AS 口座番号の下1桁,
  COUNT(*) AS データ件数
FROM
  口座
GROUP BY
  口座番号の下1桁
;

56.
SELECT
  CASE
  WHEN 更新日 IS NULL
    THEN 'XXXX年'
  ELSE
    SUBSTRING(CAST(更新日 AS VARCHAR), 1, 4)
  END AS 更新年,
  SUM(残高),
  MAX(残高),
  MIN(残高),
  AVG(残高),
  COUNT(*)
FROM
  口座
GROUP BY
  更新年
;

57.
SELECT
  種別,
  SUM(残高),
  COUNT(*)
FROM
  口座
GROUP BY
  種別
HAVING
  SUM(残高) > 3000000
;

58.
SELECT
  SUBSTRING(名義, 1, 1) AS 名義グループ,
  COUNT(*),
  AVG(LENGTH(REPLACE(名義, '　', ''))) AS 平均文字数
FROM
  口座
GROUP BY
  名義グループ
HAVING
  COUNT(*) > 10
  OR AVG(LENGTH(REPLACE(名義, '　', ''))) > 5
;

