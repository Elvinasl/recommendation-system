

class ParameterManager{

    parameters = {};

    constructor(navigator) {
        let convertedHash = navigator.convertHash();
        if(typeof convertedHash.params !== "undefined"){
            this.parameters = navigator.convertHash().params;
        }
    }

    clear(){
        this.parameters = {};
    }
    remove(parameter){
        delete this.parameters[parameter];
        navigator.updateHash();
    }
    set(parameter, data){
        this.parameters[parameter] = data;
        navigator.updateHash();
    }
    get(parameter){
        return this.parameters[parameter];
    }
    all(){
        return this.parameters;
    }

}
