using SnapTown.DTO;
using SnapTown.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace SnapTown.WebService.Converters
{
    public static class MediaConverter
    {
        public static readonly Expression<Func<Media, MediaDto>> AsMediaDto =
            x => new MediaDto()
            {
                MediaId = x.MediaID,
                Description = x.Description,
                Type = (byte)x.Type,
                UploadedBy = x.Owner.Name,
                UploadedOn = x.UploadedOn
            };
    }
}