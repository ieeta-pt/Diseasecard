import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../../api/Api";


const initialState = {
    url: '',
    invalidEndpoints: [],
    conceptsLabels: [],
    entitiesLabels: [],
    pluginsLabels: [],
    ordersLabels: [],
    resourcesLabels: [],
    resource: [],
    labels: {'conceptsLabels':[], 'resourcesLabels': [], 'entitiesLabels': [], 'ordersLabels': [], 'pluginsLabels': []},
    resourceAdded: false,
}


export const uploadOntology = createAsyncThunk('addSource/uploadOntology', async (file) => {
    return API.POST("uploadOntology", '', file ).then(res => {
        const data = res.data
        let invalidEndpoints = []

        for ( const endpoint in data ) invalidEndpoints.push({ "resource": endpoint, "invalidPath": data[endpoint] })

        return { invalidEndpoints }
    })
})


export const uploadEndpoints = createAsyncThunk('addSource/uploadEndpoints', async (information) => {
    return API.POST("uploadEndpoints", '', information ).then(res => {
        //console.log(res.data)
    })
})


export const getFormLabels = createAsyncThunk('addSource/getFormLabels', async () => {
    return API.GET("getFormLabels", '', [] ).then(res => {
        return res.data
    })
})


export const getParserFields = createAsyncThunk('addSource/getParserFields', async (parserType) => {
    return API.GET("getParserFields", '', [parserType]).then(res => {
        return res.data
    })
})


export const addEntity = createAsyncThunk('addSource/addEntity', async (form) => {
    return API.POST("addEntity", '', form ).then(res => {
        console.log("ADD ENTITY RESPONSE " + res.data)
        return res.data
    })
})


export const addConcept = createAsyncThunk('addSource/addConcept', async (form) => {
    return API.POST("addConcept", '', form ).then(res => {
        console.log("CONCEPT ADDED")
        return res.data
    })
})


export const addResource = createAsyncThunk('addSource/addResource', async (forms) => {
    let resource = forms.resource
    let parser = forms.values

    let plugin = resource.publisherEndpoint
    let label = resource.labelResource

    let formData = new FormData()
    Object.entries(resource).map(item => { formData.append(item[0], item[1]) })

    return API.POST("addResource", '', formData ).then(res => {
        return addParser(parser, label, plugin)
    })
})


export const addResourceWithURLEndpoint = createAsyncThunk('addSource/addResourceWithURLEndpoint', async (forms) => {
    let resource = forms.resource
    let parser = forms.values

    let plugin = resource.publisherEndpoint
    let label = resource.labelResource

    let formData = new FormData()
    Object.entries(resource).map(item => { formData.append(item[0], item[1]) })

    return API.POST("addResourceWithURLEndpoint", '', formData ).then(res => {
        return addParser(parser, label, plugin)
    })
})


export const addOMIMResource = createAsyncThunk('addSource/addOMIMResource', async (forms) => {
    let resource = forms.resource
    let parser = forms.values

    let formData = new FormData()
    Object.entries(resource).map(item => { formData.append(item[0], item[1]) })

    return API.POST("addOMIMResource", '', formData ).then(res => {
        //TODO: add parser!
        return res.data
    })
})


const addParser = (parser, label, plugin) => {
    let formParser = new FormData()
    Object.entries(parser).map(item => { formParser.append(item[0], item[1]) })
    formParser.append("resource", label)

    let path = ''
    if (plugin === "CSV")       path = "addCSVParser"
    else if (plugin === "XML")  path = "addXMLParser"
    else if (plugin === "OMIM") path = "addOMIMParser"

    return API.POST(path, '', formParser ).then(res => {
        return res.data
    })
}


const addSourceSlice = createSlice({
    name: 'addSource',
    initialState,
    reducers: {
        storeResource: (state, action) => {
            state.resource = action.payload
        }
    },
    extraReducers: {
        [uploadOntology.fulfilled]: (state, action) => {
            state.invalidEndpoints = action.payload.invalidEndpoints
        },
        [getFormLabels.fulfilled]: (state, action) => {

            state.labels['conceptsLabels'] = action.payload.conceptsLabels
            state.labels['resourcesLabels'] = action.payload.resourcesLabels
            state.labels['entitiesLabels'] = action.payload.entitiesLabels
            state.labels['ordersLabels'] = action.payload.ordersLabels
            state.labels['pluginsLabels'] = action.payload.pluginsLabels
        }
    }
})


export const getInvalidEndpoints = state => state.addSource.invalidEndpoints
export const getConceptsLabels = state => state.addSource.conceptsLabels
export const getEntitiesLabels = state => state.addSource.entitiesLabels
export const getPluginsLabels = state => state.addSource.pluginsLabels
export const getOrdersLabels = state => state.addSource.ordersLabels
export const getResourcesLabels  = state => state.addSource.resourcesLabels
export const getLabels = state => state.addSource.labels
export const getResource  = state => state.addSource.resource
export const getResourceAdded  = state => state.addSource.resourceAdded

export const { storeResource } = addSourceSlice.actions

export default addSourceSlice.reducer