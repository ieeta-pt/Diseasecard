import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    sources: [],
    labels: [],
    editRow: []
}


export const getSourcesURLS = createAsyncThunk('endpointManagement/getSourcesURLS', async () => {
    return API.GET("getSourcesURLS", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})


export const addSourceURL = createAsyncThunk('endpointManagement/addSourceBaseURL', async (form) => {
    return API.POST("addSourceBaseURL", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


export const editSourceURL = createAsyncThunk('endpointManagement/editSourceBaseURL', async (form) => {
    return API.POST("editSourceBaseURL", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


export const removeSourceURL = createAsyncThunk('endpointManagement/removeSourceBaseURL', async (form) => {
    return API.POST("removeSourceBaseURL", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


export const getResourcesWithoutBaseURL = createAsyncThunk('endpointManagement/getResourcesWithoutBaseURL', async (form) => {
    return API.GET("getResourcesWithoutBaseURL", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


const endpointManagementSlice = createSlice({
    name: 'endpointManagement',
    initialState,
    reducers: {
        storeEditRow: (state, action) => {
            console.log(action.payload)
            state.editRow = action.payload
        }
    },
    extraReducers: {
        [getSourcesURLS.fulfilled]: (state, action) => {
            state.sources = action.payload.results
        },
        [getResourcesWithoutBaseURL.fulfilled]: (state, action) => {
            state.labels = action.payload.results
        },
    }
})


export const getListSourcesURLS = state => state.endpointManagement.sources
export const getLabelsBaseURLS = state => state.endpointManagement.labels
export const getEditRow = state => state.endpointManagement.editRow

export const { storeEditRow } = endpointManagementSlice.actions

export default endpointManagementSlice.reducer