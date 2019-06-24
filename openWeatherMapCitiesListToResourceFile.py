# get the cities list from here: http://bulk.openweathermap.org/sample/

import json

cities = []
lat_lon = []
separator = ':';

# read file
with open('city.list.json', encoding='utf-8') as json_file:  
    data = json.load(json_file)
    for c in data:
    	if c['country'] == "DE":
    		lat, lon = c['coord']['lat'], c['coord']['lon']
    		ll = separator + 'lat=' + str(lat) + separator + 'lon=' + str(lon)
    		# only append, if not already existing
    		if ll not in lat_lon:
    			lat_lon.append(ll)
    			cities.append('name=' + c['name'] + separator + 'id=' + str(c['id']) + ll)

# write file
with open('openWeatherMapCities.xml', 'w', encoding='utf-8') as f: 
	f.write('<?xml version="1.0" encoding="utf-8"?>\n')
	f.write('<resources>\n')
	f.write('\t<string-array name="weatherMapCities">\n') 
	for city in cities:
		f.write('\t\t<item>' + city + '</item>\n')
	f.write('\t</string-array>\n')
	f.write('</resources>')