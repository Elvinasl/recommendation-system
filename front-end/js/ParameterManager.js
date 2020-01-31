

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
    }
    set(parameter, data){
        this.parameters[parameter] = data;
    }
    get(parameter){
        return this.parameters[parameter];
    }
    all(){
        return this.parameters;
    }

}
