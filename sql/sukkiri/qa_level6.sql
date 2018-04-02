59.
UPDATE
  口座
SET
  残高 = 残高 + (
	  SELECT
	    SUM(入金額) - SUM(出金額)
	  FROM
	    取引
	  WHERE
	    口座番号 = '0351333' 
	  AND 更新日 = '2013-01-11'
  )
WHERE
  口座番号 = '0351333'
;

60.
SELECT
  残高,
  (SELECT
     SUM(入金額)
   FROM
     取引
   WHERE
     口座番号 = '1115600'
     AND 日付 = '2012-12-28') AS 入金額合計,
  (SELECT
     SUM(出金額)
   FROM
     取引
   WHERE
     口座番号 = '1115600'
     AND 日付 = '2012-12-28') AS 出金額合計
FROM
  口座
WHERE
  口座番号 = '1115600'
;

61.
SELECT
  口座番号, 名義, 残高
FROM
  口座
WHERE
  口座番号 IN (SELECT 口座番号 FROM 取引 WHERE 入金額 > 1000000)
;

