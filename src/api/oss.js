import { axios } from '@/utils/request'
import { UUID } from '@/utils/util'

// {
//   createObject: '/oss/object/{bucketName}',
//   createBucket: '/oss/bucket/{bucketName}',
//   getBuckets: '/oss/bucket',
//   getBucket: '/oss/bucket/{bucketName}',
//   deleteBucket: '/oss/bucket/{bucketName}',
//   createObjectWithName: '/oss/object/{bucketName}/{objectName}',
//   filterObject: '/oss/object/{bucketName}/{objectName}',
//   getObject: '/oss/object/{bucketName}/{objectName}/{expires}',
//   deleteObject: '/oss/object/{bucketName}/{objectName}'
// }
const ossApi = {}

ossApi.createObject = (parameter) => {
  return axios({
    url: `/oss/object/violet-cloud/${UUID()}`,
    method: 'post',
    data: parameter,
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
}

export default ossApi
