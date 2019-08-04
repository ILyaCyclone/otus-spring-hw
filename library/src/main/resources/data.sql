INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'user1'),
(2, 1, 'user2'),
(3, 0, 'ROLE_ADMIN'),
(4, 0, 'ROLE_USER');


INSERT INTO acl_class (id, class, class_id_type) VALUES
(1, 'cyclone.otusspring.library.model.Book', 'java.lang.String');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, '1', NULL, 3, 0), -- book 1
(2, 1, '2', NULL, 3, 0), -- book 2
(3, 1, '3', NULL, 3, 0), -- book 3
(4, 1, '4', NULL, 3, 0), -- book 4
(5, 1, '5', NULL, 3, 0); -- book 5


-- mask: 1 - read, 2 - write, 4 - create, 8 - delete, 16 - administration
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 3, 16, 1, 1, 1), -- grant ADMINISTRATION (mask=16) to ROLE_ADMIN (sid=3) to each book
(2, 1, 3, 16, 1, 1, 1),
(3, 1, 3, 16, 1, 1, 1),
(4, 1, 3, 16, 1, 1, 1),
(5, 1, 3, 16, 1, 1, 1),

(1, 2, 1, 1, 1, 1, 1), -- grant READ (mask=1) to user1 (sid=1) to book 1, 2 and 3
(2, 2, 1, 1, 1, 1, 1),
(3, 2, 1, 1, 1, 1, 1),
(4, 2, 2, 1, 1, 1, 1), -- grant READ (mask=1) to user2 (sid=2) to book 4 and 5
(5, 2, 2, 1, 1, 1, 1);