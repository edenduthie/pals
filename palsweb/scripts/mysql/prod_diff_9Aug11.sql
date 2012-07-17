alter table VegetationType add column userAdded boolean;
update VegetationType set userAdded = false;