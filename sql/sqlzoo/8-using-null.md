# 8 Using Null

1.
```
SELECT
  name
FROM
  teacher
WHERE
  dept IS NULL
```

2.
```
SELECT
  teacher.name,
  dept.name
 FROM
  teacher
  INNER JOIN dept ON (teacher.dept=dept.id)
```

3.
```
SELECT
  teacher.name,
  dept.name
 FROM
  teacher
  LEFT JOIN dept ON (teacher.dept=dept.id)
```

4.
```
SELECT
  teacher.name,
  dept.name
 FROM
  teacher
  RIGHT JOIN dept ON (teacher.dept=dept.id)
```
