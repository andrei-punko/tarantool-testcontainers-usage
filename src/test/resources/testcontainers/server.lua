#!/usr/bin/env tarantool

box.cfg {
    listen = 3301,
    memtx_memory = 128 * 1024 * 1024, -- 128 Mb
    -- log = 'file:/tmp/tarantool.log',
    log_level = 6,
}
-- API user will be able to login with this password
box.schema.user.create('api_user', { password = 'secret' })
-- API user will be able to create spaces, add or remove data, execute functions
box.schema.user.grant('api_user', 'read,write,execute', 'universe')


s = box.schema.space.create('tester')

s:format({
    { name = 'id', type = 'unsigned' },
    { name = 'band_name', type = 'string' },
    { name = 'year', type = 'unsigned' }
})

s:create_index('primary', {
    type = 'hash',
    parts = {'id'}
})

s:insert{1, 'Roxette', 1986}
s:insert{2, 'Scorpions', 2015}
s:insert{3, 'Ace of Base', 1993}

s:create_index('secondary', {
    type = 'hash',
    parts = {'band_name'}
})
