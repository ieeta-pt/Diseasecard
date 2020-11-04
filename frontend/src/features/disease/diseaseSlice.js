import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    disease: [],
    network: [],
    omim: '',
    status: 'idle',
    error: null
}

export const getDiseaseByOMIM = createAsyncThunk('disease/getDiseaseByOMIM', async (omim) => {
    return API.GET("diseaseByOMIM", "", [omim] ).then(res => {
        // Organize data from different sources and concat their values
        let values = {};
        const data = res.data
        const network = data.network

        for (const connection in network) {
            const info = network[connection].split(":")
            if (!(info[0] in values)) values[info[0]] = []
            values[info[0]].push(info[1])
        }

        let results = []
        for (const [ key, value ] of Object.entries(values)) {
            let children = []
            for ( const index in value ) children.push( { "name" : value[index], "value":1 } )
            results.push( { "name": key, "children":children} )
        }

        return {data, results};
    })
})


const diseaseSlice = createSlice({
    name: 'disease',
    initialState,
    reducers: {},
    extraReducers: {
        [getDiseaseByOMIM.pending]: (state, action) => {
            state.status = 'loading'
            state.omim = action.meta.arg
        },
        [getDiseaseByOMIM.fulfilled]: (state, action) => {
            state.disease = action.payload.data
            state.network = action.payload.results
            state.omim = action.meta.arg
            state.status = 'succeeded'
        },
        [getDiseaseByOMIM.rejected]: (state, action) => {
            state.status = 'failed'
            state.omim = action.meta.arg
            state.error = action.error.message
        },
    }
})


export const selectOMIM = state => state.disease.omim
export const selectNetwork = state => state.disease.network
export const getStatus = state => state.disease.status
export const getDescription = state => state.disease.disease.description

export default diseaseSlice.reducer