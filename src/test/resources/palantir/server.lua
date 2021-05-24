#!/usr/bin/env tarantool

box.cfg {
    listen = 3301,
    log_level = 6,
}

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
