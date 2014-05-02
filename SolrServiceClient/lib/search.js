/**
 * responsible for querying data
 */
var Hapi = require('hapi');
var SC = require('./solrconnect.js');

exports.queryData = function(request, reply) {
    console.log('Query Solr Server.');
    var keyword = request.query.q;
    console.log(request.query);
    console.log(keyword);
    var query = SC.solrClient.createQuery()
                  .q(keyword)
                  .edismax()
                  .qf({
                      name : 3.0,
                      description : 1.0
                      })
                  .mm(2)
                  .start(0)
                  .rows(10);

        SC.solrClient.search(query, function(err, obj) {
        if (err) {
            return reply(err);
        } else {
            return reply(obj);
        }
    });
};