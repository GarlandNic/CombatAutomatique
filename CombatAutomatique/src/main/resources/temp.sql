
use azurhyan;

alter table combos
drop column ELEMENT,
add column ELEMENT enum('NORMAL','FEU','ACIDE','FROID','ELECTRICITE','PSYCHIQUE') NOT NULL DEFAULT 'NORMAL';

commit;


