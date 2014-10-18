var express = require('express')
  , http = require('http')
  , o2j = require('osm-and-geojson')
  , DOMParser = require('xmldom').DOMParser
  , app = express();

app.get('/', function(req, res) {
  var max_lat = req.param('max_lat', 0)
    , min_lat = req.param('min_lat', 0)
    , max_lon = req.param('max_lon', 0)
    , min_lon = req.param('min_lon', 0)
    , coords = [min_lon,min_lat,max_lon,max_lat]
    , url = 'http://api.openstreetmap.org/api/0.6/map?bbox='+coords.join();

  var content = "";
  var a_req = http.get(url, function(_res) {
    _res.on('data', function(chunk) {
      content += chunk;
    });

    _res.on('end', function() {
      dom = new DOMParser().parseFromString(content);
      res.send(o2j.osm2geojson(dom));
    });
  });
});

var server = app.listen(3000, function() {
  console.log("startin' this bitch on 3000");
});
