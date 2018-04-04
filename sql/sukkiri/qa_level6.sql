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

62.
SELECT
  *
FROM
  口座
WHERE
  更新日 > ALL (SELECT 日付 FROM 取引)
;

63.
SELECT
  A.日付, B.最大入金額, B.最大出金額
FROM 
  (SELECT
     日付, SUM(入金額) AS 入金額合計, SUM(出金額) AS 出金額合計
   FROM
     取引
   WHERE
     口座番号 = '3104451' GROUP BY 日付 HAVING SUM(入金額) > 0
     AND SUM(出金額) > 0) AS A,
  (SELECT
     MAX(入金額) AS 最大入金額, MAX(出金額) AS 最大出金額
   FROM
     取引
   WHERE
     口座番号 = '3104451') AS B
;

64.
INSERT INTO
  廃止口座
SELECT
  *
FROM
  口座
WHERE
  口座番号 = '2761055'
;

DELETE FROM
  口座
WHERE
  口座番号 = '2761055'
;
