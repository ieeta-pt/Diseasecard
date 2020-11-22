import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    disease: [],
    network: [],
    listOfIds: [],
    omim: '',
    status: 'idle',
    error: null,
    showFrame: false,
    url: ''
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
        let id = 1
        let listOfIds = ['root']
        let aux = ''
            for (const [ key, value ] of Object.entries(values)) {
            let children = []
            for ( const index in value ) {
                aux = key + ":" + value[index];
                listOfIds.push(aux)
                children.push( { "id": aux , "name" : value[index], "value":1 } )
            }
            results.push( { "id": id.toString(), "name": key, "children":children } )
            listOfIds.push(id.toString());
            id++;
        }
        return {data, results, listOfIds};
    })
})

export const getSourceURL = createAsyncThunk('disease/getSourceURL', async (url) => {
    return API.GET("sourceURL", url, [] ).then(res => {
        let url = res.data.url
        let protection = res.data.protection
        return { url, protection };
    })
})

const diseaseSlice = createSlice({
    name: 'disease',
    initialState,
    reducers: {
        showFrame: (state, action) => {
            state.showFrame = action.payload
        }
    },
    extraReducers: {
        [getDiseaseByOMIM.pending]: (state, action) => {
            state.status = 'loading'
            state.omim = action.meta.arg
        },
        [getDiseaseByOMIM.fulfilled]: (state, action) => {
            state.disease = action.payload.data
            state.network = action.payload.results
            state.omim = action.meta.arg
            state.listOfIds = action.payload.listOfIds
            state.status = 'succeeded'
        },
        [getDiseaseByOMIM.rejected]: (state, action) => {
            state.status = 'failed'
            state.omim = action.meta.arg
            state.error = action.error.message
        },

        [getSourceURL.pending]: (state, action) => {
            state.status = 'loading'
        },
        [getSourceURL.fulfilled]: (state, action) => {
            state.url = action.payload.url
            state.protection = action.payload.protection
            state.status = 'succeeded'
        },
        [getSourceURL.rejected]: (state, action) => {
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
export const getListOfIds = state => state.disease.listOfIds
export const getURL= state => state.disease.url
export const getProtection= state => state.disease.protection
export const showFrameSource = state => state.disease.showFrame

export const { showFrame } = diseaseSlice.actions

export default diseaseSlice.reducer