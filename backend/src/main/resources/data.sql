drop table if exists ranking;
drop view if exists ranking;
CREATE VIEW ranking AS
select
    row_number() OVER (PARTITION BY t.id_tournament ) AS position,
    t.total_score, t.username, t.id_tournament
from
(
select r.total_score, u.username,  r.id_tournament
from
    registration r, user u
where
    r.id_user = u.id
order by
    r.total_score asc
 ) t;