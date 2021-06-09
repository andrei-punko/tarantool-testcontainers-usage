#!/usr/bin/env tarantool

box.cfg {
    listen = 3301,
    memtx_memory = 128 * 1024 * 1024, -- 128 Mb
    -- log = 'file:/tmp/tarantool.log',
    log_level = 6,
}
-- API user will be able to login with this password
box.schema.user.create('api_user', { password = 'secret', if_not_exists = true })

-- API user will be able to create spaces, add or remove data, execute functions
box.schema.user.grant('api_user', 'read,write,execute', 'universe', nil, { if_not_exists = true })

tester = box.schema.space.create('tester')

tester:format({
    { name = 'id', type = 'unsigned' },
    { name = 'band_name', type = 'string' },
    { name = 'year', type = 'unsigned' }
})

tester:create_index('primary', {
    type = 'hash',
    parts = { 'id' }
})

tester:insert { 1, 'Roxette', 1986 }
tester:insert { 2, 'Scorpions', 2015 }
tester:insert { 3, 'Ace of Base', 1993 }

tester:create_index('secondary', {
    type = 'hash',
    parts = { 'band_name' }
})
