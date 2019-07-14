# 9 Self join

1.
```
SELECT
  COUNT(*)  
FROM
  stops
```

2.
```
SELECT
  id
FROM
  stops
WHERE
  name = 'Craiglockhart'
```

3.
```
SELECT
  id,
  name
FROM
  stops
  JOIN route ON stop = stops.id
WHERE
  num = 4
  AND company = 'LRT'
```

4.
```
SELECT
  company,
  num,
  COUNT(*)
FROM
  route
WHERE
  stop=149 OR stop=53
GROUP BY
  company,
  num
HAVING
  COUNT(*) >= 2
```

5.
```
SELECT
  a.company,
  a.num,
  a.stop,
  b.stop
FROM
  route a
  JOIN route b ON (a.company=b.company AND a.num=b.num)
WHERE
  a.stop=53
  AND b.stop = (SELECT id FROM stops WHERE name = 'London Road')
```

6.
```
SELECT
  a.company,
  a.num,
  stopa.name,
  stopb.name
FROM
  route a JOIN route b ON (a.company=b.company AND a.num=b.num)
  JOIN stops stopa ON (a.stop=stopa.id)
  JOIN stops stopb ON (b.stop=stopb.id)
WHERE
  stopa.name = 'Craiglockhart'
  AND stopb.name =  'London Road'
```

7.
```
SELECT
  a.company,
  a.num
FROM
  route a,
  route b
WHERE
  (a.stop = 115 AND b.stop = 137)
  AND a.num = b.num
GROUP BY
  a.company, a.num

```

8.
```
SELECT
  a.company,
  a.num
FROM
  route a
  JOIN route b ON (a.company=b.company AND a.num=b.num)
  JOIN stops stopa ON (a.stop=stopa.id)
  JOIN stops stopb ON (b.stop=stopb.id)
WHERE
  stopa.name = 'Craiglockhart'
  AND stopb.name = 'Tollcross'
```

9.
```
SELECT DISTINCT
  name,
  a.company,
  a.num
FROM
  route a
  JOIN route b ON (a.company = b.company AND a.num = b.num)
  JOIN stops ON a.stop = stops.id
WHERE
  b.stop = (
    SELECT
      id
    FROM
      stops
    WHERE
      name = 'Craiglockhart'
  )
```

10.
```
SELECT
  a.num,
  a.company,
  stopb.name,
  c.num,
  c.company
FROM
  route a
  JOIN route b ON (a.company = b.company AND a.num = b.num)
  JOIN (
    route c
    JOIN route d ON (c.company = d.company AND c.num = d.num)
  )
  JOIN stops stopa ON a.stop = stopa.id
  JOIN stops stopb ON b.stop = stopb.id
  JOIN stops stopc ON c.stop = stopc.id
  JOIN stops stopd ON d.stop = stopd.id
WHERE
  stopa.name = 'Craiglockhart'
  AND stopd.name = 'Lochend'
  AND stopb.name = stopc.name
ORDER BY
  LENGTH(a.num),
  b.num,
  stopb.name,
  LENGTH(c.num),
  d.num
```
