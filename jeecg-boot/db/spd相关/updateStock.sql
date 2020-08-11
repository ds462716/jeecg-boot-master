select * from pd_stock_record a where a.record_no = 'CK200811085039286';
select b.name,a.* from pd_stock_record_detail a LEFT JOIN pd_product b on a.product_id = b.id  where a.record_id = '1292987187254677505';
select * from pd_product_stock c where c.record_detail_id = '1292987187279843330';
select b.name,d.* from pd_stock_log d LEFT JOIN pd_product b on d.product_id = b.id where d.invoice_no = 'CK200811085039286';
select * from pd_product_stock_total a where a.product_id = '1254675931670425603';

update pd_product_stock set stock_num = 0 where id = '1292986721737265153';
update pd_product_stock set stock_num = 4 where id = '1292987187334369282';
update pd_stock_record_detail set product_num = 4 where id = '1292987155545739265';
update pd_stock_record_detail set product_num = 4 where id = '1292987187279843330';
update pd_stock_log set product_num = 4 where id = '1292987187359535106';
update pd_stock_log set product_num = 4 where id = '1292987187237900290';
update pd_product_stock_total set stock_num = 4 where id = '1292987187334369281';
update pd_product_stock_total set stock_num = 0 where id = '1292986721728876545';


