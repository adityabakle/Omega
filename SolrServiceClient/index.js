/**
 * New node file
 */

var Hapi = require('hapi');
var Search = require('./lib/search.js');
var Ping = require('./lib/ping.js');
var Log = console.log;

var routes = [ {
    method : 'GET',
    path : '/q',
    config : {
        handler : Search.queryData,
    }
}, {
    method : 'GET',
    path : '/ping',
    config : {
        handler : Ping.pingServer,
    }
} ];

var server = new Hapi.Server(8000);
server.route(routes);

server.start(function() {
    Log('Server started at: ' + server.info.uri);
});
