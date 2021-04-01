import { configureStore } from '@reduxjs/toolkit';
import templateReducer from '../template/templateSlice';
import addSourceSlice from '../features/management/addSource/addSourceSlice';
import listResourceSlice from '../features/management/listResources/listResourcesSlice';

import { reducer as formReducer } from 'redux-form';

export default configureStore({
  reducer: {
    template: templateReducer,
    addSource: addSourceSlice,
    listResources: listResourceSlice,
    form: formReducer
  }
});
