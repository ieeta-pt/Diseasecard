import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../../api/Api";


const initialState = {
    url: '',
    invalidEndpoints: [],
    conceptsLabels: [],
    entitiesLabels: [],
    pluginsLabels: [],
    resourcesLabels: []
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


export const addEntity = createAsyncThunk('addSource/addEntity', async (form) => {
    return API.POST("addEntity", '', form ).then(res => {
        return res.data
    })
})


export const addConcept = createAsyncThunk('addSource/addConcept', async (form) => {
    return API.POST("addConcept", '', form ).then(res => {
        return res.data
    })
})


export const addResource = createAsyncThunk('addSource/addResource', async (form) => {
    return API.POST("addResource", '', form ).then(res => {
        return res.data
    })
})

export const addResourceWithURLEndpoint = createAsyncThunk('addSource/addResourceWithURLEndpoint', async (form) => {
    return API.POST("addResourceWithURLEndpoint", '', form ).then(res => {
        return res.data
    })
})


const addSourceSlice = createSlice({
    name: 'addSource',
    initialState,
    reducers: {},
    extraReducers: {
        [uploadOntology.pending]: (state, action) => {
           /*state.status = 'loading'*/
        },
        [uploadOntology.fulfilled]: (state, action) => {
            state.invalidEndpoints = action.payload.invalidEndpoints
            /*state.url = action.payload.url
            state.protection = action.payload.protection
            state.status = 'succeeded'*/
        },
        [uploadOntology.rejected]: (state, action) => {
            /*state.status = 'failed'
            state.omim = action.meta.arg
            state.error = action.error.message*/
        },
        [getFormLabels.fulfilled]: (state, action) => {
            //console.log(action.payload.conceptsLabels)
            state.conceptsLabels = action.payload.conceptsLabels;
            state.entitiesLabels = action.payload.entitiesLabels;
            state.pluginsLabels = action.payload.pluginsLabels;
            state.resourcesLabels = action.payload.resourcesLabels;
        },
    }
})


export const getInvalidEndpoints = state => state.addSource.invalidEndpoints
export const getConceptsLabels = state => state.addSource.conceptsLabels
export const getEntitiesLabels = state => state.addSource.entitiesLabels
export const getPluginsLabels = state => state.addSource.pluginsLabels
export const getResourcesLabels  = state => state.addSource.resourcesLabels

/*
export const selectOMIM = state => state.disease.omim
*/

export default addSourceSlice.reducer