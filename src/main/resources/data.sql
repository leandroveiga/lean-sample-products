INSERT INTO category (id, name, parent_category_id)
  VALUES
    (1, 'Mobile', null),
    (2, 'Android', 1),
    (3, 'iOS', 1),
    (4, 'Windows Phone', 1),
    (5, '16Gb', null);

INSERT INTO product (name, description, price, available)
  VALUES
    ('Nexus 9', 'Powerful Google tablet', 680, 68),
    ('Nexus 7', 'Slim Google tablet', 380, 8),
    ('Nexus 6', 'Newest Google phablet', 350, 5),
    ('Nexus 5', 'Newest Google phone', 300, 5),
    ('Nexus 4', 'First 4" Google phone', 290, 0),
    ('Lumia 520', 'Inexpensive Windows Phone', 50, 45),
    ('Lumia 920', 'Powerful Windows Phone', 550, 4),
    ('Moto Maxx', 'Powerful Motorola phablet', 100, 35),
    ('Moto X', 'Powerful Motorola phone', 100, 35),
    ('Moto E', 'Cheap Motorola phone', 100, 35),
    ('Moto G', 'Handy device from Motorola', 200, 15);

INSERT INTO product_categories (product_id, categories_id)
  VALUES
    (1, 2),
    (2, 2);