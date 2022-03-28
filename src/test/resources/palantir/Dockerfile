FROM tarantool/tarantool:2.6.0
COPY base.lua /opt/tarantool
COPY 01-spaces-indexes.lua /opt/tarantool
COPY 02-users-grants.lua /opt/tarantool
COPY 03-data.lua /opt/tarantool
CMD ["tarantool", "/opt/tarantool/base.lua"]
