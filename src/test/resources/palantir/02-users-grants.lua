
-- API user will be able to login with this password
box.schema.user.create('api_user', { password = 'secret', if_not_exists = true })

-- API user will be able to create spaces, add or remove data, execute functions
box.schema.user.grant('api_user', 'read,write,execute', 'universe', nil, { if_not_exists = true })
