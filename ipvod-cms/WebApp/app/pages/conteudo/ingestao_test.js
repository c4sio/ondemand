describe('Conteúdo: Ingestão', function() {
    'use strict';
    var ingestaoService;
    beforeEach(function () {
        module('vod');
        inject(function (_ingestaoService_) {
            ingestaoService = _ingestaoService_;
        });        
    });
    
    it('Returned data must be JSON', function() {
        ingestaoService.then(function (data) { 
            expect(typeof data).to.be.an('object');
        });
    });
    
    it('Returned data must have products', function() {
//        ingestaoService.then(function (data) {
//            expect(data.products.length).to.be.at.least(4);
            expect(true).to.be(false);
//        });
    });
});