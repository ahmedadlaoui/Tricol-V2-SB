--liquibase formatted sql

--changeset ismailbaguni:001-create-suppliers-table runOnChange:true
CREATE TABLE IF NOT EXISTS suppliers (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         company_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    ice VARCHAR(255) NOT NULL UNIQUE,
    contact_person VARCHAR(255) NOT NULL
    );

--changeset ismailbaguni:002-create-products-table runOnChange:true
CREATE TABLE IF NOT EXISTS products (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        reference VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    unit_price DECIMAL(19, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    current_stock DECIMAL(19, 4) NOT NULL DEFAULT 0.0,
    reorder_point DECIMAL(19, 4) NOT NULL DEFAULT 0.0,
    unit_of_measure VARCHAR(255) NOT NULL
    );

--changeset ismailbaguni:003-create-goods-issues-table runOnChange:true
CREATE TABLE IF NOT EXISTS goods_issues (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            issue_number VARCHAR(255) NOT NULL UNIQUE,
    issue_date DATE NOT NULL,
    destination VARCHAR(255) NOT NULL,
    motif VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    validation_date TIMESTAMP
    );

--changeset ismailbaguni:004-create-goods-issue-lines-table runOnChange:true
CREATE TABLE IF NOT EXISTS goods_issue_lines (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 quantity DECIMAL(19, 4) NOT NULL,
    goods_issue_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_goods_issue_lines_goods_issue FOREIGN KEY (goods_issue_id) REFERENCES goods_issues(id),
    CONSTRAINT fk_goods_issue_lines_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

--changeset ismailbaguni:005-create-purchase-orders-table runOnChange:true
CREATE TABLE IF NOT EXISTS purchase_orders (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               order_date DATE NOT NULL,
                                               status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(19, 2),
    supplier_id BIGINT NOT NULL,
    reception_date TIMESTAMP,
    CONSTRAINT fk_purchase_orders_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
    );

--changeset ismailbaguni:006-create-purchase-order-lines-table runOnChange:true
CREATE TABLE IF NOT EXISTS purchase_order_lines (
                                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                    quantity DECIMAL(19, 4) NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    sub_total DECIMAL(19, 2),
    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_purchase_order_lines_purchase_order FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    CONSTRAINT fk_purchase_order_lines_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

--changeset ismailbaguni:007-create-stock-lots-table runOnChange:true
CREATE TABLE IF NOT EXISTS stock_lots (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          lot_number VARCHAR(255) NOT NULL UNIQUE,
    entry_date DATE NOT NULL,
    purchase_price DECIMAL(19, 2) NOT NULL,
    remaining_quantity DECIMAL(19, 4) NOT NULL,
    initial_quantity DECIMAL(19, 4) NOT NULL,
    product_id BIGINT NOT NULL,
    purchase_order_line_id BIGINT NOT NULL,
    CONSTRAINT fk_stock_lots_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_stock_lots_purchase_order_line FOREIGN KEY (purchase_order_line_id) REFERENCES purchase_order_lines(id)
    );

--changeset ismailbaguni:008-create-stock-movements-table runOnChange:true
CREATE TABLE IF NOT EXISTS stock_movements (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               movement_date DATE NOT NULL,
                                               quantity DECIMAL(19, 4) NOT NULL,
    movement_type VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL,
    stock_lot_id BIGINT,
    goods_issue_line_id BIGINT,
    purchase_order_line_id BIGINT,
    CONSTRAINT fk_stock_movements_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_stock_movements_stock_lot FOREIGN KEY (stock_lot_id) REFERENCES stock_lots(id),
    CONSTRAINT fk_stock_movements_goods_issue_line FOREIGN KEY (goods_issue_line_id) REFERENCES goods_issue_lines(id),
    CONSTRAINT fk_stock_movements_purchase_order_line FOREIGN KEY (purchase_order_line_id) REFERENCES purchase_order_lines(id)
    );