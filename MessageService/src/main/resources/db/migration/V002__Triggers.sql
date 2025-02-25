CREATE OR REPLACE FUNCTION set_created_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.created_at := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER before_add_chat
BEFORE INSERT ON chats
FOR EACH ROW
EXECUTE FUNCTION set_created_at();

CREATE TRIGGER before_add_message
BEFORE INSERT ON messages
FOR EACH ROW
EXECUTE FUNCTION set_created_at();