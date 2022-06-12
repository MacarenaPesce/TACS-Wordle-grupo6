drop table if exists wordle.ranking;
drop view if exists wordle.ranking;
CREATE VIEW wordle.ranking AS
select
	t.id,
	row_number() OVER (PARTITION BY t.id_tournament ) AS position,
    t.total_score, t.username, t.id_tournament, t.last_submitted_score
from
(
select ROW_NUMBER() OVER w as id,
r.total_score, u.username,  r.id_tournament, r.last_submitted_score
from
    wordle.registration r, wordle.user u
where
    r.id_user = u.id
WINDOW w AS (ORDER BY r.id_tournament)
order by
    r.total_score asc
 ) t;