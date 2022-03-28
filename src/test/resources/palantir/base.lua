#!/usr/bin/env tarantool

-- Start listening connections on 3301 port
box.cfg {
    listen = 3301,
    memtx_memory = 128 * 1024 * 1024, -- 128 Mb
    log_level = 6,
}

-- Apply changes from scripts
dofile('01-spaces-indexes.lua')
dofile('02-users-grants.lua')
dofile('03-data.lua')
