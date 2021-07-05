import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    disease: [],
    network: [],
    listOfIds: [],
    omim: '',
    status: '',
    ready: '',
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
        network.push("OMIM:" + data.omim)

        for (const connection in network) {
            const info = network[connection].split(/:(.+)/)
            if (info.length > 1) {
                if (!(info[0] in values)) values[info[0]] = []
                values[info[0]].push(info[1].replaceAll(":", "_"))
            }
        }

        let results = []
        let id = 1
        let listOfIds = ['root']
        let aux = ''
        for (const [ key, value ] of Object.entries(values)) {
            if (value !== "") {
                let children = []
                for ( const index in value ) {
                    aux = key + ":" + value[index];
                    listOfIds.push(aux)
                    if (value[index].length >= 8)   children.push( { "id": aux , "fullName": value[index], "name" : value[index].substring(0,7) + "...", "value":1 } )
                    else                            children.push( { "id": aux , "fullName": value[index], "name" : value[index], "value":1 } )

                }
                if (key==='omim') results.push( { "id": id.toString(), "fullName":'OMIM', "name": 'OMIM', "children":children } )
                else results.push( { "id": id.toString(), "fullName":key, "name": key, "children": children } )
                listOfIds.push(id.toString());
                id++;
            }
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
            state.status = 'succeeded'
            state.disease = action.payload.data
            state.network = action.payload.results
            state.omim = action.meta.arg
            state.listOfIds = action.payload.listOfIds
            state.ready = 'go'
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
export const getReady = state => state.disease.ready
export const getDescription = state => state.disease.disease.description
export const getListOfIds = state => state.disease.listOfIds
export const getURL= state => state.disease.url
export const getProtection= state => state.disease.protection
export const showFrameSource = state => state.disease.showFrame

export const { showFrame } = diseaseSlice.actions

export default diseaseSlice.reducer