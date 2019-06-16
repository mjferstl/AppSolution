# get the cities list from here: http://bulk.openweathermap.org/sample/

import json

cities = []

# read file
with open('city.list.json', encoding='utf-8') as json_file:  
    data = json.load(json_file)
    for c in data:
    	if c['country'] == "DE":
    		cities.append(c['name'] + ':' + str(c['id']))

# write file
with open('openWeatherMapCityCodes.xml', 'w', encoding='utf-8') as f: 
	f.write('<?xml version="1.0" encoding="utf-8"?>\n')
	f.write('<resources>\n')
	f.write('\t<string-array name="owmCityCodex">\n') 
	for city in cities:
		f.write('\t\t<item>' + city + '</item>\n')
	f.write('\t</string-array>\n')
	f.write('</resources>')