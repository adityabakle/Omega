/**
 * New node file
 */
var Solr = require('solr-client');

exports.solrClient = Solr.createClient({
    host : 'localhost',
    port : '8983',
    core : '',
    path : '/solr'
});