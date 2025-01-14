
use azurhyan;
alter table personnages
drop column COULEUR;

commit;

mysql -u root -p

use azurhyan;

alter table combos add column BOUCLIERQUALIT2 integer unsigned NOT NULL DEFAULT 0;
