# 7 More JOIN operations

1.
```
SELECT
  id,
  title
FROM
  movie
WHERE
  yr=1962
```

2.
```
SELECT
  yr
FROM
  movie
WHERE
  title =  'Citizen Kane'
```

3.
```
SELECT
  id,
  title,
  yr
FROM
  movie
WHERE
  title LIKE  '%Star Trek%'
ORDER BY
  yr
```

4.
```
SELECT
  id
FROM
  actor
WHERE
  name = 'Glenn Close'
```

5.
```
SELECT
  id
FROM
  movie
WHERE
  title = 'Casablanca'
```

6.
```
SELECT
  actor.name
FROM
  casting
  JOIN actor ON casting.actorid = actor.id
WHERE
  movieid=11768
```

7.
```
SELECT
  actor.name
FROM
  casting
  JOIN actor ON casting.actorid = actor.id
  JOIN movie ON casting.movieid = movie.id
WHERE
  movie.title = 'Alien'
```

8.
```
SELECT
  movie.title
FROM
  casting
  JOIN actor ON casting.actorid = actor.id
  JOIN movie ON casting.movieid = movie.id
WHERE
  actor.name = 'Harrison Ford'
```

9.
```
SELECT
  movie.title
FROM
  casting
  JOIN actor ON casting.actorid = actor.id
  JOIN movie ON casting.movieid = movie.id
WHERE
  actor.name = 'Harrison Ford'
  AND casting.ord != 1
```

10.
```
SELECT
  movie.title,
  actor.name
FROM
  casting
  JOIN actor ON casting.actorid = actor.id
  JOIN movie ON casting.movieid = movie.id
WHERE
  movie.yr = 1962
  AND casting.ord = 1
```

11.
```
SELECT
  yr,
  COUNT(title)
FROM
  movie 
  JOIN casting ON movie.id=movieid
  JOIN actor ON actorid=actor.id
WHERE
  name='John Travolta'
GROUP BY
  yr
HAVING
  COUNT(title) = (
    SELECT
      MAX(c)
    FROM
      (
        SELECT
          yr,
          COUNT(title) AS c
        FROM
          movie JOIN casting ON movie.id=movieid
         JOIN actor ON actorid=actor.id
        WHERE
          name='John Travolta'
        GROUP BY
          yr
      ) AS t
  )
```

12.
```
SELECT
  title,
  name
FROM
  casting
  JOIN movie ON (movieid = movie.id AND ord = 1)
  JOIN actor ON actorid = actor.id
WHERE
  movie.id IN (
    SELECT
      movieid
    FROM
      casting
    WHERE
      actorid IN (
        SELECT
          id
        FROM
          actor
        WHERE
          name='Julie Andrews'        
      )
  )
```

13.
```
SELECT
  DISTINCT name
FROM
  actor
  JOIN casting ON actorid = actor.id
  JOIN movie ON movieid = movie.id
WHERE
  casting.actorid IN (
     SELECT
      actorid
     FROM
      casting
     WHERE
      ord = 1
     GROUP BY
      actorid
     HAVING
      COUNT(actorid) >= 30
  )
ORDER BY
  name
```

14.
```
SELECT
  title,
  COUNT(actorid)
FROM
  movie
  JOIN casting ON movieid = movie.id
WHERE
  yr = 1978
GROUP BY
  title
ORDER BY
  COUNT(actorid) DESC, title
```

15.
```
SELECT DISTINCT
  name
FROM
  actor
  JOIN casting ON actorid = actor.id
  JOIN movie ON movieid = movie.id
WHERE
  name != 'Art Garfunkel'
  AND movieid IN (
    SELECT
      movie.id
    FROM
      movie
      JOIN casting ON movieid = movie.id
      JOIN actor ON actorid = actor.id
    WHERE
      actor.name = 'Art Garfunkel'
  )
```
