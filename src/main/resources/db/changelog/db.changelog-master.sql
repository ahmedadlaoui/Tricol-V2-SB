--liquibase formatted sql

--include file:tables/001-create-suppliers-table.sql
--include file:tables/002-create-products-table.sql
--include file:tables/003-create-goods-issues-table.sql
--include file:tables/004-create-goods-issue-lines-table.sql
--include file:tables/005-create-purchase-orders-table.sql
--include file:tables/006-create-purchase-order-lines-table.sql
--include file:tables/007-create-stock-lots-table.sql
--include file:tables/008-create-stock-movements-table.sql