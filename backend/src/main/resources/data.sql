drop table if exists ranking;
drop view if exists ranking;
CREATE VIEW ranking AS
select
	t.id,
	row_number() OVER (PARTITION BY t.id_tournament ) AS position,
    t.total_score, t.username, t.id_tournament, t.last_submitted_score
from
(
select ROW_NUMBER() OVER w as id,
r.total_score, u.username,  r.id_tournament, r.last_submitted_score
from
    registration r, user u
where
    r.id_user = u.id
WINDOW w AS (ORDER BY r.id_tournament)
order by
    r.total_score asc
 ) t;