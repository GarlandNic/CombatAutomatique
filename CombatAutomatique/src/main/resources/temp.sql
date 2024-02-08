
use azurhyan;

alter table combos
drop column ELEMENT,
add column ELEMENT enum('NORMAL','FEU','ACIDE','FROID','ELECTRICITE','NECROTIQUE') NOT NULL DEFAULT 'NORMAL';

use azurhyan;
alter table blessures
drop FOREIGN KEY blessures_ibfk_2,
drop column REFACTION,
add column REFACTION integer DEFAULT 0;

use azurhyan;
alter table actions
drop column CIBLEID,
add column CIBLEID integer default 0,
drop column CIBLEDE,
add column CIBLEDE integer default 0;

commit;


