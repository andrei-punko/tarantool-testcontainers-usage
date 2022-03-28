
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

tester:create_index('secondary', {
    type = 'hash',
    parts = { 'band_name' }
})
