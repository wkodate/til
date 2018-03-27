9.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 口座番号 = '0037651';

10.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 残高 > 0;

11.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 口座番号 < '1000000';

12.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 更新日 < '2013-01-01';

13.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 残高 >= 1000000;

14.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 種別 != '1';

15.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 更新日 IS NULL;

16.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 名義 LIKE '%ハシ%';

17.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 更新日 BETWEEN '2013-01-01' AND '2013-01-31';

18.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 種別 IN('2', '3');

19.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 名義 = 'サカタ　リョウヘイ' OR 名義 = 'マツモト　ミワコ' OR 名義 = 'ハマダ　サトシ';

20.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 更新日 BETWEEN '2012-12-30' AND '2013-01-04';

21.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 残高 < 10000 AND 更新日 IS NOT NULL;

22.
SELECT 口座番号, 名義, 種別, 残高, 更新日 FROM 口座 WHERE 口座番号 LIKE '2%' AND 名義 LIKE 'エ__　%コ';

23.
口座テーブル: 口座番号
取引テーブル: 取引番号
取引事由テーブル: 取引事由ID
