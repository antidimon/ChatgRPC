CREATE OR REPLACE FUNCTION set_created_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.created_at := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER before_add_user
BEFORE INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION set_created_at();