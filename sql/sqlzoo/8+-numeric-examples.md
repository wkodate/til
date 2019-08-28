# 8+ Numeric Examples

1.
```
SELECT
  A_STRONGLY_AGREE
FROM
  nss
WHERE
  question='Q01'
  AND institution='Edinburgh Napier University'
  AND subject='(8) Computer Science'
```

2.
```
SELECT
  institution,
  subject
FROM
  nss
WHERE
  question='Q15'
  AND score >= 100
```

3.
```
SELECT
  institution,score
FROM
  nss
WHERE
  question='Q15'
  AND score < 50
  AND subject='(8) Computer Science'
```

4.
```
SELECT
  subject,
  SUM(response)
FROM
  nss
WHERE
  question='Q22'
  AND (subject='(8) Computer Science' OR subject='(H) Creative Arts and Design')
GROUP BY
  subject
```

5.
```
SELECT
  subject,
  SUM(A_STRONGLY_AGREE*response/100)
FROM
  nss
WHERE
  question='Q22'
  AND (subject='(8) Computer Science' OR subject='(H) Creative Arts and Design')
GROUP BY
  subject
```

6.
```
SELECT
  subject,
  ROUND(SUM(A_STRONGLY_AGREE*response/100)/SUM(response)*100, 0)
FROM
  nss
WHERE
  question='Q22'
  AND (subject='(8) Computer Science' OR subject='(H) Creative Arts and Design')
GROUP BY
  subject
```

7.
```
SELECT
  institution,
  ROUND(SUM(score*response)/SUM(response), 0)
FROM
  nss
WHERE
  question='Q22'
  AND (institution LIKE '%Manchester%')
GROUP BY
  institution
ORDER BY
  institution
```

8.
```
SELECT
  institution,
  SUM(sample),
  ( 
    SELECT
      sample
    FROM
      nss
      y
    WHERE
      subject='(8) Computer Science'
      AND x.institution = y.institution
      AND question='Q01'
    ) AS comp
FROM
  nss x
WHERE
  question='Q01'
  AND (institution LIKE '%Manchester%')
GROUP BY
  institution
ORDER BY
  institution
```