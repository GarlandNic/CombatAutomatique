
use azurhyan;

alter table combos
MODIFY COLUMN TYPEDGTS enum('NOR','CTD','PRF','GLB','BOUSCUL','CAPTURE');

commit;


