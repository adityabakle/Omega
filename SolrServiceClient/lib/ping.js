/**
 * responsible for querying data
 */
var SC = require('./solrconnect.js');
var Log = console.log;

exports.pingServer = function(request, reply) {
    Log('Ping Solr Server');

    SC.solrClient.ping(function(err, obj) {
        if (err) {
            return reply(err);
        } else {
            return reply(obj);
        }
    });
};