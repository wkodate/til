24.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 ORDER BY 口座番号;

25.
SELECT DISTINCT 名義 FROM 口座 ORDER BY 名義;

26.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 ORDER BY 4 DESC, 1;

27.
SELECT 更新日 FROM 口座 WHERE 更新日 IS NOT NULL ORDER BY 更新日 LIMIT 10;

28.
SELECT 更新日, 残高 FROM 口座 WHERE 残高 > 0 AND 更新日 IS NOT NULL ORDER BY 残高, 更新日 DESC LIMIT 10 OFFSET 10;

29.
SELECT 口座番号 FROM 口座 UNION SELECT 口座番号 FROM 廃止口座 ORDER BY 口座番号;

30.
SELECT 名義 FROM 口座 EXCEPT SELECT 名義 FROM 廃止口座 ORDER BY 名義 DESC;

31.
SELECT 名義 FROM 口座 INTERSECT SELECT 名義 FROM 廃止口座 ORDER BY 名義;

32.
SELECT 口座番号, 残高 FROM 口座 WHERE 残高 = 0 UNION SELECT 口座番号, 解約時残高 FROM 廃止口座  WHERE 解約時残高 <> 0 ORDER BY 口座番号;

33.
SELECT 口座番号, 名義, '◯' AS 有効な口座 FROM 口座 UNION SELECT 口座番号, 名義, '×' AS 有効な口座 FROM 廃止口座 ORDER BY 名義;
