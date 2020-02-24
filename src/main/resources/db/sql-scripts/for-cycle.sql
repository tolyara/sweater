DO $$
BEGIN
FOR counter IN 18..25 LOOP
	insert into message (id, text, tag, user_id) values (counter, 'this is spam myhaha ', 'bulk ins', 7);
END LOOP;
END; $$