document.getElementById('file').addEventListener('change',getFile);
function getFile(event){
    const input = event.target
    if('files' in input && input.files.length > 0){
        placeFileContent(document.getElementById('content-target'),input.files[0])
    }
}
function placeFileContent(target,file){
    readFileContent(file).then(content => { myCodeMirror.setValue(content) }).catch(error => console.log(error))
}

function readFileContent(file){
    const reader = new FileReader()
    return new Promise((resolve, reject) => {
        reader.onload = event => resolve(event.target.result)
    reader.onerror = error => reject(error)
    reader.readAsText(file)
});
}