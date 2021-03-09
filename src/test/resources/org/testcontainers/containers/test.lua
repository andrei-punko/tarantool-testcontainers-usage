function user_function_no_param()
    return 5;
end

function create_band(id, name, year)
    box.space.tester:insert { id, name, year }
end

function get_band(id)
    return box.space.tester:select { id };
end

function delete_band(id)
    box.space.tester:delete(id)
end

function update_band_name(id, name)
    box.space.tester:update(id, { { '=', 2, name } })
end

function update_band_year(id, year)
    box.space.tester:update(id, { { '=', 3, year } })
end

function update_band_name_and_year(id, name, year)
    box.space.tester:update(id, { { '=', 2, name }, { '=', 3, year } })
end
